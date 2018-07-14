package com.github.schuettec.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.github.schuettec.remote.Service;

public class GameServer {

	public static void main(String[] args) throws Exception {

		Server server = new Server();
		Kryo kryo = server.getKryo();
		// Register classes if needed
		kryo.register(Service.class);
		ObjectSpace.registerClasses(kryo);
		server.start();
		server.bind(54555, 54777);

		ObjectSpace objectSpace = new ObjectSpace();
		objectSpace.register(42, new ServiceImpl());

		server.addListener(new Listener() {
			@Override
			public void connected(Connection connection) {
				objectSpace.addConnection(connection);
			}
		});

	}

}
