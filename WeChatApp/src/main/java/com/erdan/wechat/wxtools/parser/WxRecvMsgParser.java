package com.erdan.wechat.wxtools.parser;

import org.jdom.Document;
import org.jdom.JDOMException;

import com.erdan.wechat.wxtools.vo.recv.WxRecvMsg;


public interface WxRecvMsgParser {
	WxRecvMsg parser(Document doc) throws JDOMException;
}
