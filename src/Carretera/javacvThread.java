package Carretera;

import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;
import com.googlecode.javacpp.Loader;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import static com.googlecode.javacv.cpp.opencv_core.cvFlip;
import java.nio.ByteBuffer;
import com.googlecode.javacv.cpp.opencv_highgui;
import com.googlecode.javacv.*;
import com.googlecode.javacv.cpp.*;
import java.awt.event.KeyEvent;

public class javacvThread implements Runnable {
    private static javacvThread javacv;
    private static int num_c1=0, num_c2=0, avanza_x=50, cambio_c1=0, cambio_c2=0;

    public static int getCambio_c1() {
        return cambio_c1;
    }

    public static int getCambio_c2() {
        return cambio_c2;
    }
    
    private javacvThread(){}
    
    public static javacvThread getInstance() { 
    if(javacv==null) 

     javacv = new javacvThread(); 

     return javacv; 

    }  
    
    public static void setCoches(int c1, int c2){
        num_c1 = c1;
        num_c2 = c2;
    }
    
     public static int getC1(){

        return num_c1;

    }

   

    public static int getC2(){

        return num_c2;

    }
    
    public static boolean isColorOk(IplImage img, int x, int y, int carril) {
        ByteBuffer buffImgR = img.getByteBuffer(2);
        ByteBuffer buffImgG = img.getByteBuffer(1);
        ByteBuffer buffImgB = img.getByteBuffer(0);
        
        double r=0,g=0,b=0;
        int index = y * img.widthStep() + img.nChannels()*x;
        r = buffImgR.get(index) & 0xff;
        g = buffImgG.get(index) & 0xff;
        b = buffImgB.get(index) & 0xff;
        
        if(x+75 <= img.width()){
            ByteBuffer buffImgR2 = img.getByteBuffer(2);
            ByteBuffer buffImgG2 = img.getByteBuffer(1);
            ByteBuffer buffImgB2 = img.getByteBuffer(0);
            
            double r2=0,g2=0,b2=0;
            int index2 = y * img.widthStep() + img.nChannels()*(x+75);
            r2 = buffImgR2.get(index2) & 0xff;
            g2 = buffImgG2.get(index2) & 0xff;
            b2 = buffImgB2.get(index2) & 0xff;
            if(carril==1){
                if(b2 < 10/*90*/ && g2 < 100) {
                    cambio_c1++;  
                }
                else{
                    if(cambio_c1 != 0) cambio_c1--;
                }
            }else if(carril == 2){
                if(b2 < 25/*90*/ && g2 < 20) {
                    cambio_c2++;
                }
                else{
                    if(cambio_c2 != 0) cambio_c2--;
                }
            }
        }
        
	if(carril==1){
            if(b < 10/*90*/ && g < 100) {
                return true;
            }
            return false;
        }
        else if(carril==2){
            if(b <= 25/*160*/ && g < 20/*100*/) {
                return true;
            }
            return false;
        }
        return false;

    }
    
    public void run(){
        IplImage img=null;
	int x,y,h=1;
	int hay=0;
	int coches1=0,coches2=0;
        boolean encontrado=false;
	CvCapture cap = opencv_highgui.cvCreateCameraCapture(1);
	CanvasFrame canvas = new CanvasFrame("Carretera");
        canvas.setCanvasSize(320, 240);
        CvMemStorage storage = cvCreateMemStorage(0);
	while(h==1){
            encontrado=false;
	    coches1=0;
	    coches2=0;
            //pedir imagen a la camara
            img = cvQueryFrame(cap);
            if(img == null) break;
            
            if(encontrado==false){
                for(y=35; y<46 && encontrado==false; y++){
                    for(x=0; x<img.width();x++){
                        if(isColorOk(img, x, y,1)){
                            coches1++;
                            x+=avanza_x;
                            encontrado = true;
                        }
                    }
                }
            }
            
            encontrado = false;
            
            if(encontrado==false){
                for(y=440; y<451 && encontrado==false; y++){
                    for(x=0; x<img.width();x++){
                        if(isColorOk(img, x, y,2)){
                            coches2++;
                            x+=avanza_x;
                            encontrado = true;
                        }
                    }
                }
            }
            
            
            opencv_highgui.cvSaveImage("imagen_carr.jpg", img);      
            String S_Coches1 = Integer.toString(coches1-1);
            String S_Coches2 = Integer.toString(coches2);
            CvFont font= new CvFont();
            double hScale=1.5;
            double vScale=1.5;
            //int lineWidth=8;
            setCoches(coches1, coches2);
            cvInitFont(font,CV_FONT_HERSHEY_SIMPLEX, hScale,vScale,0,3,CV_AA);
            cvPutText(img,S_Coches1,cvPoint(20,120), font, CV_RGB(0,0,0));
            cvPutText (img,S_Coches2,cvPoint(20,180), font, CV_RGB(0,0,0));
            canvas.showImage(img);
            KeyEvent key = canvas.waitKey(10);
            if (key != null) return;
        }
        cvReleaseImage(img);
        cvReleaseCapture(cap);
        cvClearMemStorage( storage );
        cvDestroyWindow("Carretera");
    }
    
  
}
