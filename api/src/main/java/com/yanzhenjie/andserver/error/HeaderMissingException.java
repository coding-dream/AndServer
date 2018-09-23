/*
 * Copyright 2018 Yan Zhenjie.
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
package com.yanzhenjie.andserver.error;

import com.yanzhenjie.andserver.util.StatusCode;

/**
 * Created by YanZhenjie on 2018/9/9.
 */
public class HeaderMissingException extends BasicException {

    private static final String MESSAGE = "Missing header [%s] for method parameter.";

    public HeaderMissingException(String name) {
        super(StatusCode.SC_BAD_REQUEST, String.format(MESSAGE, name));
    }

    public HeaderMissingException(String name, Throwable cause) {
        super(StatusCode.SC_BAD_REQUEST, String.format(MESSAGE, name), cause);
    }

    public HeaderMissingException(Throwable cause) {
        super(StatusCode.SC_BAD_REQUEST, String.format(MESSAGE, ""), cause);
    }
}