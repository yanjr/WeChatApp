package com.erdan.wechat.wxtools.parser;

import org.jdom.Element;
import org.jdom.JDOMException;

import com.erdan.wechat.wxtools.vo.recv.WxRecvEventMsg;
import com.erdan.wechat.wxtools.vo.recv.WxRecvMsg;


public class WxRecvEventMsgParser extends WxRecvMsgBaseParser {

	@Override
	protected WxRecvEventMsg parser(Element root, WxRecvMsg msg) throws JDOMException {
		String event = getElementText(root, "Event");
		String eventKey = getElementText(root, "EventKey");
		
		return new WxRecvEventMsg(msg, event,eventKey);
	}

}
