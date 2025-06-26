package com.example.server.config;

import com.example.server.accessors.ThreadLocalAccessor;
import com.example.server.context.ContextPropagationManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@Configuration
public class ExecutorConfig {

    @Bean
    public ContextPropagationManager contextPropagationManager(List<ThreadLocalAccessor<?>> accessors) {
        return new ContextPropagationManager(accessors);
    }

    @Bean
    public TaskDecorator contextPropagatingTaskDecorator(ContextPropagationManager contextManager) {
        return runnable -> {
            System.out.println("TaskDecorator.decorate() called - Thread: " + Thread.currentThread().getName());

            // Capture ALL contexts from current thread (main request thread)
            Map<String, Object> capturedContext = contextManager.captureContext();
            System.out.println("Captured context keys: " + capturedContext.keySet());

            return () -> {
                System.out.println("TaskDecorator lambda executing - Thread: " + Thread.currentThread().getName());
                try {
                    // Restore ALL contexts in the new async thread
                    contextManager.restoreContext(capturedContext);
                    runnable.run();
                } finally {
                    // Clean up ALL contexts in the async thread
                    contextManager.clearContext();
                }
            };
        };
    }

    @Bean
    public Executor taskExecutor(TaskDecorator contextPropagatingTaskDecorator) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("tool-exec-");
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(100);
        executor.setTaskDecorator(contextPropagatingTaskDecorator);
        executor.initialize();
        return executor;
    }
}
