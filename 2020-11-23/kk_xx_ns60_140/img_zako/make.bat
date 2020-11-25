@ECHO OFF

del *.dat
del *.jar

REM ### リストをみてファイルをアーカイブ ###
..\tools\mycopy.pl /o /F /Y img_zako.list img_zako.dat

REM ### JARアーカイブ ###
jar -cfM img_zako.jar img_zako.dat

pause
