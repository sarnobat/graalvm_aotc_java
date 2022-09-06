/*
 * Copyright (c) 2010, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package com;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.json.JSONException;
import java.io.IOException;

/**
 *
 * @author Jakub Podlesak
 */
	@javax.ws.rs.Path("helloworld")
public class HelloWorldResource { // Must be public
    public static final String CLICHED_MESSAGE = "Hello World!";

		@GET
		@javax.ws.rs.Path("helloworld")
    @Produces(MediaType.TEXT_PLAIN)
    public String getHello() {
System.err.println("list()");
//System.out.println(iValue);
        return CLICHED_MESSAGE;
    }

}
