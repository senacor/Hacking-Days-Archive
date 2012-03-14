package com.hd11.process.ihu.server;
import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.drools.task.service.TaskServerHandler;

public class IhuBaseServer implements Runnable{

	    private final int port;

	    IoHandlerAdapter  handler;

	    IoAcceptor        acceptor;
	    String ip;
	    volatile boolean  running;

	    public IhuBaseServer(IoHandlerAdapter handler,
	                          int port,String ip) {
	        this.handler = handler;
	        this.port = port;
	        this.ip = ip;
	    }


		public void run() {
	        try {
	            start();
	            while ( running ) {
	                Thread.sleep( 100 );
	            }
	        } catch ( Exception e ) {
	            throw new RuntimeException( "Server Exception with class " + getClass() + " using port " + port,
	                                        e );
	        }
	    }

	    public void start() throws IOException {
	        running = true;

	        acceptor = new NioSocketAcceptor();

	        acceptor.getFilterChain().addLast( "logger",
	                                           new LoggingFilter() );
	        acceptor.getFilterChain().addLast( "codec",
	                                           new ProtocolCodecFilter( new ObjectSerializationCodecFactory() ) );

	        acceptor.setHandler( handler );
	        acceptor.getSessionConfig().setReadBufferSize( 2048 );
	        acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE,
	                                                 10 );
	        acceptor.bind( new InetSocketAddress( ip, port ) );
	    }
	    
	    public IoAcceptor getIoAcceptor() {
	        return acceptor;
	    }

	    public void stop() {
	        acceptor.dispose();
	        running = false;
	    }
	
	
}
