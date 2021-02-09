-keepclassmembernames class * {
    @com.uphyca.galette.SendScreenView *;
    @com.uphyca.galette.SendEvent *;
}

-keepclassmembers class * implements com.uphyca.galette.FieldBuilder {
   <init>();
}
