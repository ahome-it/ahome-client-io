/*
 * Copyright (c) 2014 Ahomé Innovation Technologies. All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ait.toolkit.clientio.client.eventhandling;

import com.google.gwt.user.client.Window;

/**
 * Default Client IO initialization handler
 */
public class DefaultClientIOInitHandler implements ClientIOInitHandler {

    @Override
    public void onInit() {

    }

    @Override
    public void onInitError() {
        Window.alert( "Flash seems to not be installed or activated. Please install or enable Flash to use ClientIO" );
    }

}
