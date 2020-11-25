@ECHO OFF

del *.dat
del *.jar

REM ### リストをみてファイルをアーカイブ ###
..\tools\mycopy.pl /o /F /Y img_map.list img_map.dat

REM ### JARアーカイブ ###
jar -cfM img_map.jar img_map.dat

pause
