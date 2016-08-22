/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.leftshift.weather;


/**
 * Runs {@link UseCase}s using a {@link UseCaseScheduler}.
 */
public class UseCaseHandler {

    private static UseCaseHandler INSTANCE;

    private final UseCaseScheduler mUseCaseScheduler;

    /**
     * Instantiates a new Use case handler.
     *
     * @param useCaseScheduler the use case scheduler
     */
    public UseCaseHandler(UseCaseScheduler useCaseScheduler) {
        mUseCaseScheduler = useCaseScheduler;
    }

    /**
     * Execute.
     *
     * @param <T>      the type parameter
     * @param <R>      the type parameter
     * @param useCase  the use case
     * @param values   the values
     * @param callback the callback
     */
    public <T extends UseCase.RequestValues, R extends UseCase.ResponseValue> void execute(
            final UseCase<T, R> useCase, T values, UseCase.UseCaseCallback<R> callback) {
        useCase.setRequestValues(values);
        useCase.setUseCaseCallback(new UiCallbackWrapper(callback, this));

        mUseCaseScheduler.execute(new Runnable() {
            @Override
            public void run() {

                useCase.run();
            }
        });
    }

    /**
     * Notify response.
     *
     * @param <V>             the type parameter
     * @param response        the response
     * @param useCaseCallback the use case callback
     */
    public <V extends UseCase.ResponseValue> void notifyResponse(final V response,
            final UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.notifyResponse(response, useCaseCallback);
    }

    private <V extends UseCase.ResponseValue> void notifyError(
            final UseCase.UseCaseCallback<V> useCaseCallback) {
        mUseCaseScheduler.onError(useCaseCallback);
    }

    private static final class UiCallbackWrapper<V extends UseCase.ResponseValue> implements
            UseCase.UseCaseCallback<V> {
        private final UseCase.UseCaseCallback<V> mCallback;
        private final UseCaseHandler mUseCaseHandler;

        /**
         * Instantiates a new Ui callback wrapper.
         *
         * @param callback       the callback
         * @param useCaseHandler the use case handler
         */
        public UiCallbackWrapper(UseCase.UseCaseCallback<V> callback,
                UseCaseHandler useCaseHandler) {
            mCallback = callback;
            mUseCaseHandler = useCaseHandler;
        }

        @Override
        public void onSuccess(V response) {
            mUseCaseHandler.notifyResponse(response, mCallback);
        }

        @Override
        public void onError() {
            mUseCaseHandler.notifyError(mCallback);
        }
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static UseCaseHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UseCaseHandler(new UseCaseThreadPoolScheduler());
        }
        return INSTANCE;
    }
}
