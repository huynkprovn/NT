RUN_DIR=.

MYAPP_LIBS=$RUN_DIR/lib
MYAPP_CLASSPATH=$RUN_DIR/bin
MYAPP_CLASSPATH=$MYAPP_CLASSPATH:$MYAPP_LIBS/*

$JAVA_HOME/bin/java -Xms512m -Xmx1024m -cp $MYAPP_CLASSPATH  com.vht.sms.content.ContentListener