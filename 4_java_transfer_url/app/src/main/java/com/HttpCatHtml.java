package com;
/*
 * Copyright (c) 2010, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;

import com.sun.net.httpserver.HttpExchange;
//import com.sun.net.httpserver.HttpHandler;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;

/**
 * This is the example entry point, where Jersey application for the example
 * gets populated and published using the Grizzly 2 HTTP container.
 *
 * @author Marek Potociar
 */
public class HttpCatHtml {

	/**
	 * "Hello World" root resource path.
	 */
	public static final String ROOT_PATH = "";

	/**
	 * Main application entry point.
	 *
	 * @param args application arguments.
	 */
	public static void main(String[] args) {
		try {
			System.out.println("\"Hello World\" Jersey Example App");
			String port = "4466";
			if (args.length > 0) {
				port = args[0];
			}
			URI BASE_URI = URI.create("http://0.0.0.0:" + port + "/");
			final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, true);

			
			HttpHandler h = new HttpHandler() {
//				public void service(Request request, Response response) throws Exception {
//					response.setContentType("text/plain");
//					response.setContentLength(date.length());
//					response.getWriter().write("helloworld");
//				}

//				@Override
//				public void handle(HttpExchange e) throws IOException {
//
//					byte[] response = "DONE".getBytes();
//					e.sendResponseHeaders(200, response.length);
//					OutputStream os = e.getResponseBody();
//					os.write(response);
//					os.close();
//				}

				@Override
				public void service(Request request, org.glassfish.grizzly.http.server.Response response)
						throws Exception {
					String iValue =
					request.getParameter("value");
					
					System.err.println("list()");
					System.out.println(iValue);
					response.setContentType("text/html");
//					String str = "<u>helloworld</u>";
					// Red: CC0033
					String str = "<body style=\"background-color: #99CC33\">Success:<br><a href='" + iValue + "'>" + iValue + "</a></body>";
					response.setContentLength(str.length());
					response.getWriter().write(str);					
				}
			};
			server.getServerConfiguration().addHttpHandler(h, "/");
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					server.shutdownNow();
				}
			}));
			server.start();

			System.out.println(
					String.format("Application started.%n" + "Try out %s%s%n" + "Stop the application using CTRL+C",
							BASE_URI, ROOT_PATH));

			Thread.currentThread().join();
		} catch (IOException | InterruptedException ex) {
			Logger.getLogger("App").log(Level.SEVERE, null, ex);
		}

	}

	/**
	 * Test assertion indicator that a GET method handler has been called.
	 */
	public static volatile boolean getMethodCalled = false;
	/**
	 * Test assertion indicator that a HEAD method handler has been called.
	 */
	public static volatile boolean headMethodCalled = false;

	/**
	 * Create example application resource configuration.
	 *
	 * @return initialized resource configuration of the example application.
	 */
	public static ResourceConfig create() {
		final Resource.Builder resourceBuilder = Resource.builder(ROOT_PATH);

		resourceBuilder.addMethod("GET").handledBy(new Inflector<ContainerRequestContext, Response>() {

			@Override
			public Response apply(ContainerRequestContext data) {
				String iValue = data.getUriInfo().getQueryParameters().getFirst("value");
				System.err.println("list()");
				System.out.println(iValue);
				getMethodCalled = true;
				return Response.ok("<html><body>hi</body></html>").header("Access-Control-Allow-Origin", "*")
						.type("text/html").build();
			}
		});

		Inflector<ContainerRequestContext, Response> noContentResponder = new Inflector<ContainerRequestContext, Response>() {

			@Override
			public Response apply(ContainerRequestContext data) {
				headMethodCalled = true;
				return Response.noContent().build();
			}
		};
		resourceBuilder.addMethod("HEAD").handledBy(noContentResponder);
		resourceBuilder.addMethod("OPTIONS").handledBy(noContentResponder);

		return new ResourceConfig().registerResources(resourceBuilder.build());
	}
}
