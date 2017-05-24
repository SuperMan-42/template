package com.recorder.log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Collection;

public class WSocketServer extends WebSocketServer {

    public WSocketServer( int port ) throws UnknownHostException {
        super( new InetSocketAddress( port ) );
    }

    public WSocketServer( InetSocketAddress address ) {
        super( address );
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        this.sendToClient( "[Handshake Ok]");
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        this.sendToClient(webSocket + " close" );
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        this.sendToClient(message );
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        e.printStackTrace();
        if( webSocket != null ) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    public void sendToClient( String text ) {
        Collection<WebSocket> con = connections();
        synchronized ( con ) {
            for( WebSocket c : con ) {
                c.send( text );
            }
        }
    }
}
