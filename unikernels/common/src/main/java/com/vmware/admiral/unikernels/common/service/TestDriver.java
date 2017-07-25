/*
 * Copyright (c) 2017 VMware, Inc. All Rights Reserved.
 *
 * This product is licensed to you under the Apache License, Version 2.0 (the "License").
 * You may not use this product except in compliance with the License.
 *
 * This product may include a number of subcomponents with separate copyright notices
 * and license terms. Your use of these subcomponents is subject to the terms and
 * conditions of the subcomponent's license, as noted in the LICENSE file.
 */

package com.vmware.admiral.unikernels.common.service;

import com.vmware.xenon.common.ServiceHost;

public class TestDriver {


    public static void main(String[] args) throws Throwable {

        ServiceHost host = ServiceHost.create();
        host.initialize(args);
        host.start();
        host.startDefaultCoreServicesSynchronously();
        host.startService(new TranslateDockerService());
        host.startService(new CompilationSuccessHandlerService());
        host.startService(new DownloadRequestService());
        host.startService(new CompilationFailureHandlerService());
        host.startFactory(new UnikernelCreationTaskService());
    }
}