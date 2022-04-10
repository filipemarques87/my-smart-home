package io.mysmarthome.util;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public final class SneakyException {

    public static <T, R> Function<T, R> sneakyException(SneakyFunction<T, R, Exception> throwingFunction) {
        return i -> {
            try {
                return throwingFunction.apply(i);
            } catch (Exception ex) {
                log.error("Error while calling function", ex);
                throw new RuntimeException(ex);
            }
        };
    }

    public static <T> Supplier<T> sneakyException(SneakySupplier<T, Exception> throwingSupplier) {
        return () -> {
            try {
                return throwingSupplier.get();
            } catch (Exception ex) {
                log.error("Error while calling supplier", ex);
                throw new RuntimeException(ex);
            }
        };
    }

    public static Runnable sneakyException(SneakyRunnable<Exception> throwingSupplier) {
        return () -> {
            try {
                throwingSupplier.run();
            } catch (Exception ex) {
                log.error("Error while calling runnable", ex);
                throw new RuntimeException(ex);
            }
        };
    }

    public static <T> Consumer<T> sneakyException(SneakyConsumer<T, Exception> throwingConsumer) {
        return a -> {
            try {
                throwingConsumer.accept(a);
            } catch (Exception ex) {
                log.error("Error while calling runnable", ex);
                throw new RuntimeException(ex);
            }
        };
    }
}
