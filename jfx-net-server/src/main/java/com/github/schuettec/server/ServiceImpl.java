package com.github.schuettec.server;

import com.github.schuettec.remote.Service;

public class ServiceImpl implements Service {

	@Override
	public String echo(String message) {
		return new StringBuilder(message).reverse().toString();
	}

}
