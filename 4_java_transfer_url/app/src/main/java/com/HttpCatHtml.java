package com;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

/**
 * This is the example entry point, where Jersey application for the example
 * gets populated and published using the Grizzly 2 HTTP container.
 *
 * @author Marek Potociar
 */
public class HttpCatHtml {

	public static final String ROOT_PATH = "";

	public static void main(String[] args) {
		try {
			System.err.println("\"Hello World\" Jersey Example App");
			String port = "4466";
			if (args.length > 0) {
				port = args[0];
			}
			URI BASE_URI = URI.create("http://0.0.0.0:" + port + "/");
			final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, true);

			HttpHandler h = new HttpHandler() {

				@Override
				public void service(Request request, org.glassfish.grizzly.http.server.Response response)
						throws Exception {
					String iValue = request.getParameter("value");

					System.err.println("list()");
					System.out.println(iValue);
					response.setContentType("text/html");
					// Red: CC0033
					String str = "<body style=\"background-color: #99CC33\">Success:<br><a href='" + iValue + "'>"
							+ iValue + "</a></body>";
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

			System.err.println(
					String.format("Application started.%n" + "Try out %s%s%n" + "Stop the application using CTRL+C",
							BASE_URI, ROOT_PATH));

			Thread.currentThread().join();
		} catch (IOException | InterruptedException ex) {
			Logger.getLogger("App").log(Level.SEVERE, null, ex);
		}

	}

}
