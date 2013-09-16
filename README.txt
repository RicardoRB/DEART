Instalaci�n de software necesario para el uso del programa.

NetBeans:
- Ir al enlace: http://netbeans.org/ y descargar la �ltima versi�n de NetBeans
- Instalar siguiendo el asistente.

JAVA:
- Ir a la p�gina: http://java.com/es/download/ y descargar la �ltima versi�n de JAVA 32 bits.
- Instalar siguiendo el asistente.

OPENCV
|
|WINDOWS:
|- Ir a la p�gina: http://opencv.willowgarage.com/wiki/ y descargar la versi�n de OpenCV seg�n el sistema operativo usado.
|- Ejecutar el archivo y descomprimir en C:
|- Ir a Equipo -> Propiedades -> Configuraci�n avanzada del sistema -> Opciones avanzadas -> Variables de entorno...
|	- A�adir dos variables:
|	1. OpenCV_bin: C:\OpenCV\build\x86\mingw\bin
|	2. OpenCV_lib: C:\OpenCV\build\x86\mingw\lib
|
|LINUX:
|-En linea de comandos:
|	- sudo apt-get install build-essential cmake subversion libgtk2.0-dev
|	- pkg-config
|	- sudo apt-get install libpng12-0 libpng12-dev libjpeg62 libjpeg62-dev zlib1g zlib1g-dev libtiff4 libtiff4-dev libjasper1 libavcodec52 libdc1394-22

JavaCV:
- Ir a la p�gina: http://code.google.com/p/javacv/downloads/list y descargar el paquete javacv-bin-20120218.zip
- Descomprimir
- A�adir los archivos .jar a la carpeta de nuestro proyecto.