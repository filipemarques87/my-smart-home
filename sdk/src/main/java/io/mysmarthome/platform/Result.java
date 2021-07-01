package io.mysmarthome.platform;


//import lombok.Builder;
//import lombok.NoArgsConstructor;
//import lombok.Value;

import java.util.function.BiFunction;
import java.util.function.Consumer;

//@NoArgsConstructor
public class Result {


//    public Result(ReceivedMessage<T> message, Exception exception) {
//        this.message = message;
//        this.exception = exception;
//    }

    public void onReceive(BiFunction<ReceivedMessage, Exception, Void> f) {

    }
//    ReceivedMessage<T> message;
//    Exception exception;

//    public Result onSuccess(Consumer<ReceivedMessage<?>> f) {
//        if (exception == null) {
//            f.accept(message);
//        }
//        return this;
//    }
//
//    public void onError(Consumer<Exception> f) {
//        if (exception != null) {
//            f.accept(exception);
//        }
//    }
}
