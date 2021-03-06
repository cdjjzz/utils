package https;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;
import java.util.logging.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLEngineResult.HandshakeStatus;
import javax.net.ssl.SSLEngineResult.Status;
import javax.net.ssl.SSLSession;

public class SSLSocketChanel extends SocketChannel{
	private static Logger logger = Logger.getLogger(SSLSocketChanel.class.getName());
	private SocketChannel socketChannel;
    private SSLEngine sslEngine;
    private Selector selector;
    private HandshakeStatus hsStatus;
    private Status status;
    private ByteBuffer myNetData;
    private ByteBuffer myAppData;
    private ByteBuffer peerNetData;
    private ByteBuffer peerAppData;
	protected SSLSocketChanel(SelectorProvider provider,Selector selector) throws Exception {
		super(provider);
		socketChannel=provider.openSocketChannel();
		SSLContext sslContext = SSLContext.getInstance("SSLv3");
        sslContext.init(null, null, null);
        sslEngine = sslContext.createSSLEngine();
        sslEngine.setUseClientMode(true);
        SSLSession session = sslEngine.getSession();
        String[] supportedProtocols = sslEngine.getSupportedProtocols();
        for (String protocol : supportedProtocols) {
        	System.out.println(protocol);
        }
        myAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        myNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        peerAppData = ByteBuffer.allocate(session.getApplicationBufferSize());
        peerNetData = ByteBuffer.allocate(session.getPacketBufferSize());
        peerNetData.clear();
        this.selector=selector;
	}

	@Override
	public <T> T getOption(SocketOption<T> name) throws IOException {
		return socketChannel.getOption(name);
	}

	@Override
	public Set<SocketOption<?>> supportedOptions() {
		return socketChannel.supportedOptions();
	}

	@Override
	public SocketChannel bind(SocketAddress local) throws IOException {
		return socketChannel.bind(local);
	}

	@Override
	public <T> SocketChannel setOption(SocketOption<T> name, T value) throws IOException {
		return socketChannel.setOption(name, value);
	}

	@Override
	public SocketChannel shutdownInput() throws IOException {
		return socketChannel.shutdownInput();
	}

	@Override
	public SocketChannel shutdownOutput() throws IOException {
		return socketChannel.shutdownOutput();
	}

	@Override
	public Socket socket() {
		return socketChannel.socket();
	}

	@Override
	public boolean isConnected() {
		return socketChannel.isConnected();
	}

	@Override
	public boolean isConnectionPending() {
		return socketChannel.isConnectionPending();
	}

	@Override
	public boolean connect(SocketAddress remote) throws IOException {
		socketChannel.connect(remote);
		if(socketChannel.isConnectionPending()){
			socketChannel.finishConnect();
		}
		sslEngine.beginHandshake();
		hsStatus=sslEngine.getHandshakeStatus();
		doHandshake();
		return true;
	}

	@Override
	public boolean finishConnect() throws IOException {
		return socketChannel.finishConnect();
	}

	@Override
	public SocketAddress getRemoteAddress() throws IOException {
		return socketChannel.getRemoteAddress();
	}

	@Override
	public int read(ByteBuffer dst) throws IOException {
		 return socketChannel.read(dst);
	}

	@Override
	public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
		 return socketChannel.read(dsts, offset, length);
	}

	@Override
	public int write(ByteBuffer src) throws IOException {
		return socketChannel.write(src);
	}

	@Override
	public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
		return socketChannel.write(srcs, offset, length);
	}

	@Override
	public SocketAddress getLocalAddress() throws IOException {
		return socketChannel.getLocalAddress();
	}

	@Override
	protected void implCloseSelectableChannel() throws IOException {
	}

	@Override
	protected void implConfigureBlocking(boolean block) throws IOException {
		// TODO Auto-generated method stub
		
	}
	public void configureBlocking(boolean block,String parames) throws IOException{
		socketChannel.configureBlocking(block);
	}
	public void register(Selector sel, int ops,String parames) throws IOException{
		socketChannel.register(sel,ops);
	}
    public  int doHandshake() throws IOException {
        SSLEngineResult result;
        int count = 0;
        while (hsStatus != HandshakeStatus.FINISHED) {
            logger.info("handshake status: " + hsStatus);
            switch (hsStatus) {
            case NEED_TASK:
                Runnable runnable;
                while ((runnable = sslEngine.getDelegatedTask()) != null) {
                	runnable.run();
                }
                hsStatus = sslEngine.getHandshakeStatus();
                break;
            case NEED_UNWRAP:
                count = socketChannel.read(peerNetData);
                if (count < 0) {
                    logger.info("no data is read for unwrap.");
                    break;
                } else {
                    logger.info("data read: " + count);
                }
                peerNetData.flip();
                peerAppData.clear();
                do {
                    result = sslEngine.unwrap(peerNetData, peerAppData);
                    logger.info("Unwrapping:\n" + result);
                } while (result.getStatus() == SSLEngineResult.Status.OK
                        && result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.NEED_UNWRAP);           
                if (peerAppData.position() == 0 && result.getStatus() == SSLEngineResult.Status.OK
                        && peerNetData.hasRemaining()) {
                    result = sslEngine.unwrap(peerNetData, peerAppData);
                    logger.info("Unwrapping:\n" + result);
                }
                hsStatus = result.getHandshakeStatus();
                status = result.getStatus();
                // Prepare the buffer to be written again.
                peerNetData.compact();
                // And the app buffer to be read.
                peerAppData.flip();
                break;
            case NEED_WRAP:
                myNetData.clear();
                result = sslEngine.wrap(myAppData, myNetData);
                hsStatus = result.getHandshakeStatus();
                status = result.getStatus();
                while (status != Status.OK) {
                    logger.info("status: " + status);
                    switch (status) {
                    case BUFFER_OVERFLOW:
                        break;
                    case BUFFER_UNDERFLOW:
                        break;
                     default:
                     	break;
                    }
                }
                myNetData.flip();
                count = socketChannel.write(myNetData);
                if (count <= 0) {
                    logger.info("No data is written.");
                } else {
                    logger.info("Written data: " + count);
                }
                break;
			default:
				break;
            }
        }
        return count;
    }

	public SSLEngine getSslEngine() {
		return sslEngine;
	}

	public void setSslEngine(SSLEngine sslEngine) {
		this.sslEngine = sslEngine;
	}
	public void handleSocketEvent(SelectionKey key) throws IOException {
		//if(key.isValid())return;
		System.out.println(key);
        if (key.isConnectable()) {
            if (socketChannel.isConnectionPending()) {
            	socketChannel.finishConnect();
            }
            doHandshake();
            socketChannel.register(selector,SelectionKey.OP_WRITE);
        }
        if (key.isReadable()) {
            doHandshake();
            socketChannel.register(selector, SelectionKey.OP_WRITE);
            if (hsStatus == HandshakeStatus.FINISHED) {
                logger.info("Client handshake completes... ...");
                key.cancel();
                socketChannel.close();
            }
        }
        if(key.isWritable()){
        	doHandshake();
        	socketChannel.register(selector, SelectionKey.OP_READ);
        }
    }

	public HandshakeStatus getHsStatus() {
		return hsStatus;
	}

	public void setHsStatus(HandshakeStatus hsStatus) {
		this.hsStatus = hsStatus;
	}

	public ByteBuffer getMyAppData() {
		return myAppData;
	}

	public void setMyAppData(ByteBuffer myAppData) {
		this.myAppData = myAppData;
	}

	public ByteBuffer getPeerNetData() {
		return peerNetData;
	}

	public void setPeerNetData(ByteBuffer peerNetData) {
		this.peerNetData = peerNetData;
	}

	public ByteBuffer getMyNetData() {
		return myNetData;
	}

	public void setMyNetData(ByteBuffer myNetData) {
		this.myNetData = myNetData;
	}

	public ByteBuffer getPeerAppData() {
		return peerAppData;
	}

	public void setPeerAppData(ByteBuffer peerAppData) {
		this.peerAppData = peerAppData;
	}
	
	
	
	
	

}
