/*
 * Copyright 2011 Moxie Group
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

package com.ait.toolkit.clientio.uploader.client.events;

import com.ait.toolkit.clientio.uploader.client.File;

/**
 * Provides access to the raw information provided by Uploader when an 'uploader progress' event occurs.
 * This class should not be instantiated directly, but instead you should create a {@link UploadProgressHandler} and
 * register it via the {@link com.ait.toolkit.clientio.uploader.client.Uploader#setUploadProgressHandler(UploadProgressHandler)}
 * method in order to access progress events.
 * <p/>
 * Note that the 'uploadProgress' event is fired periodically by the Flash Control. This event is useful for
 * providing UI updates on the page.
 * <p/>
 * Note: The Linux Flash Player fires a single uploadProgress event after the entire file has been uploaded.
 * This is a bug in the Linux Flash Player that we cannot work around.
 *
 * @author squinn@moxiegroup.com (Shawn Quinn)
 */
public class UploadProgressEvent extends UploadEvent {

    private long bytesComplete;
    private long bytesTotal;

    /**
     * This constructor is intended for internal use only.  You should not create progress events
     * directly, but instead should register a {@link UploadProgressHandler}.
     *
     * @param file The native javascript object containing the details of the file being uploaded.
     * @param bytesComplete Bytes transferred since the uploader process began.
     * @param bytesTotal Total size of the file being uploaded.
     */
    public UploadProgressEvent(File file, long bytesComplete, long bytesTotal) {
        super(file);
        this.bytesComplete = bytesComplete;
        this.bytesTotal = bytesTotal;
    }

    /**
     * Return the bytes transferred since the uploader process began.
     * @return double
     */
    public long getBytesComplete() {
        return bytesComplete;
    }

    /**
     * Return the total size of the file being uploaded.
     * @return long
     */
    public long getBytesTotal() {
        return bytesTotal;
    }

}
