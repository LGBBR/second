@ECHO OFF

del *.dat
del *.jar

REM ### リストをみてファイルをアーカイブ ###
..\tools\mycopy.pl /o /F /Y img.list img.dat

REM ### JARアーカイブ ###
jar -cfM img.jar img.dat

pause
