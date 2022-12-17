package com;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.process.Inflector;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.model.Resource;

import org.glassfish.grizzly.http.server.HttpServer;

/**
 * This is the example entry point, where Jersey application for the example
 * gets populated and published using the Grizzly 2 HTTP container.
 *
 * @author Marek Potociar
 */
public class HttpCatWithWrite {

	private static String filepath = System.getProperty("user.home") + "/db.git/yurl_flatfile_db/yurl_queue_httpcat.txt";

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
			String port = "4465";
			if (args.length > 0) {
				port = args[0];
			}
			URI BASE_URI = URI.create("http://0.0.0.0:"+port+"/");
			final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, create(), true);
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				@Override
				public void run() {
					server.shutdownNow();
				}
			}));
			server.start();

			System.out.println(
			    String.format("Application started.%n"
			                  + "Try out %s%s%n"
			                  + "Stop the application using CTRL+C",
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
				String iKey = data.getUriInfo().getQueryParameters().getFirst("key");
				String iCategoryId = data.getUriInfo().getQueryParameters().getFirst("categoryId");
				System.err.println("list()");
try {
			String line = iCategoryId + "::" + System.currentTimeMillis() + "::" + iValue;
			System.err.println("Writing to stdout: " + line);
			// I wish I didn't have to do this in Java but I found that even though
			// the browser was returning success, nothing was getting written to the
			// file.
			System.err.println("[DEBUG] about to write to file: " + filepath);
			FileUtils.write(Paths.get(filepath).toFile(), line + "\n", true);
			System.err.println("[DEBUG] wrote to file");
			System.out.println(line);
				return Response.ok().header("Access-Control-Allow-Origin", "*").type("application/json").build();
} catch (Exception e) {
throw new RuntimeException(e);
}

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
