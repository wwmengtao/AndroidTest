package com.mt.androidtest.tool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import android.content.Context;
import android.util.Xml;
import com.mt.androidtest.ALog;

public class XmlOperator {
	private XmlPullParser mXmlPullParser = null;
	private XmlSerializer mXmlSerializer = null;
	private String ioEncoding=null;
	private String namespace = null;
	private InputStream mInputStream=null;
	private FileOutputStream mFileOutputStream = null;
	private Context mContext=null;
	//
	private String fileName=null;
	private String tagOfDoc=null;
	private String eleName=null;
	private String attrName=null;
	//
	public XmlOperator(Context context){
		mContext=context.getApplicationContext();
		ioEncoding = StandardCharsets.UTF_8.name();
		mXmlPullParser = Xml.newPullParser();
		mXmlSerializer  = Xml.newSerializer();
	}
	
	public void howToWriteAndReadXml(Context context){
		String fileName="writeXml.xml";
		String tagOfDoc="tagOfDoc";
		String eleName="eleName";
		String attr="attr";
		XmlOperator mXmlOperator=new XmlOperator(context);
		mXmlOperator.setInfomation(fileName, tagOfDoc, eleName, attr);
		mXmlOperator.writeToXml();
		mXmlOperator.readFromXml();
	}
	
	public void setInfomation(String fileName,String tagOfDoc,String eleName,String attrName){
		this.fileName=fileName;
		this.tagOfDoc=tagOfDoc;
		this.eleName=eleName;
		this.attrName=attrName;
	}
	
	//检查文件读写所需信息是否完整
	public void checkInfomation(){
		if(null==tagOfDoc||null==eleName||null==attrName){
			throw new IllegalArgumentException("File info incomplete!");
		}
	}
	
	public void writeToXml(){
		checkInfomation();
		startWrite(mFileOutputStream,fileName,tagOfDoc);
		writeContents();
		endWrite(tagOfDoc);
	}
	
	public void startWrite(FileOutputStream mFileOutputStream,String fileName,String tag_Doc){
		try {
			mFileOutputStream=mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
			mXmlSerializer.setOutput(new BufferedOutputStream(mFileOutputStream), ioEncoding);
			mXmlSerializer.startDocument(ioEncoding, true);
			mXmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			mXmlSerializer.startTag(namespace, tag_Doc);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void writeContents(){
		for(int i=0;i<5;i++){
			stag(eleName);
			attr(attrName, Integer.toString(i));
			etag(eleName);
		}
	}
	
	public void endWrite(String tag_Doc){
		try {
	    	mXmlSerializer.endTag(namespace, tag_Doc);
	    	mXmlSerializer.endDocument();
	    	mXmlSerializer.flush();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
	    	if(null!=mFileOutputStream){
	            try {
	            	mFileOutputStream.close();
	            	mFileOutputStream=null;
	            } catch (Exception e) {
	            	e.printStackTrace();
	            }
	    	}
		}
	}
	
	public void stag(String tag){
		try {
			mXmlSerializer.startTag(namespace, tag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void attr(String attrName,String attrValue){
		try {
			mXmlSerializer.attribute(namespace,attrName,attrValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  void etag(String tag){
		try {
			mXmlSerializer.endTag(namespace, tag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public void readFromXml(){
		checkInfomation();
		try {
			mInputStream=mContext.openFileInput(fileName);
			mXmlPullParser.setInput(new BufferedInputStream(mInputStream), ioEncoding);
			//String tagOfDoc ="Languages";
			//String eleName="language";
			filterBeforeFirstElement(mXmlPullParser, tagOfDoc);
			readContents(mXmlPullParser,eleName,attrName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
	    	if(null!=mInputStream){
	            try {
	            	mInputStream.close();
	            	mInputStream=null;
	            } catch (Exception e) {
	            	e.printStackTrace();
	            }
	    	}
		}
	}
	
    private void readContents(XmlPullParser parser,String tag_name,String attr) throws IOException, XmlPullParserException {
        final int outerDepth = parser.getDepth();
        String attrValue=null;
        /*--------------------------读xml的时候顺便写入特定内容------------------------*/
        while (expectedElement(parser, outerDepth)) {
        	if (parser.getName().equals(tag_name)) {
        		attrValue = parser.getAttributeValue(namespace, attr);
        		ALog.Log("Value of "+attr+":"+attrValue);
        	}
        }
    }
	
    //filterBeforefirstElement：过滤掉达到指定标签之前的所有内容
    public void filterBeforeFirstElement(XmlPullParser parser, String firstElementName) throws XmlPullParserException, IOException
    {
        int type;
        //首先过滤掉非标签类事件
        while ((type=parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {
        	;
        }
        //已经到了END_DOCUMENT
        if (type != XmlPullParser.START_TAG) {
            throw new XmlPullParserException("No start tag");
        }
        //已经到了START_TAG
        if (!parser.getName().equals(firstElementName)) {
            throw new XmlPullParserException("Unexpected start tag: "+parser.getName()+", expected: " + firstElementName);
        }
    }
	

    //expectedElement：获取特定深度的element内容
    public boolean expectedElement(XmlPullParser parser, int outerDepth) throws IOException, XmlPullParserException {
    	/**
    	 * int END_DOCUMENT = 1;
    	 * int START_TAG = 2;
    	 * int END_TAG = 3;
    	 * int TEXT = 4;
    	 */
        for (;;) {
            int type = parser.next();
            //下列条件表明已经到文件末尾或者到达深度outerDepth标签结尾
            if (type == XmlPullParser.END_DOCUMENT || (type == XmlPullParser.END_TAG && parser.getDepth() == outerDepth)) {
                return false;
            }
            //下列条件表明已经到文件特定标签的开始位置
            if (type == XmlPullParser.START_TAG) {
            	if(parser.getDepth() == outerDepth+1){//+1表示仅仅解析深度outerDepth+1的标签内容
            		return true;
            	}else{//不解析深度大于outerDepth+1的标签，比如嵌套标签
            		return false;
            	}
            }
        }
    }

}
