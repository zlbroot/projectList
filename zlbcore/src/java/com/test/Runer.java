package com.test;

import com.zlb.core.kit.server.JettyServer;


public class Runer {
	public static void main(String[] args) {
		JettyServer server = new JettyServer("src/web", 80, "/", 10);
		server.start();
	}

}
