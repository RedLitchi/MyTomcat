package com.nieqiang.java;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
// �����ж��Ƿ���Ҫ�ر�����
	private boolean shutdown = false;

	public void acceptWait() {
		ServerSocket serverSocket = null;
		try {
//�˿ںţ������������ip��ַ
			serverSocket = new ServerSocket(8080, 1, InetAddress.getByName("127.0.0.1"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		// �ȴ��û�������
		while (!shutdown) {
			try {
				Socket socket = serverSocket.accept();
				InputStream is = socket.getInputStream();
				OutputStream os = socket.getOutputStream();
				// �����������
				Request request = new Request(is);
				request.parse();
				// �������ڷ���������Ķ���
				Response response = new Response(os);
				response.setRequest(request);
				response.sendStaticResource();
				// �ر�һ�������socket,��Ϊhttp������ǲ��ö����ӵķ�ʽ
				socket.close();
				// ��������ַ��/shutdown ��ر�����
				if (null != request) {
					shutdown = request.getUrL().equals("/shutdown");
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}

	public static void main(String[] args) {
		HttpServer server = new HttpServer();
		server.acceptWait();
	}
}