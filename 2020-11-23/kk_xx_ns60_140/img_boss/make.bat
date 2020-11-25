@ECHO OFF

del *.dat
del *.jar

REM ### リストをみてファイルをアーカイブ ###
..\tools\mycopy.pl /o /F /Y img_boss.list img_boss.dat

REM ### JARアーカイブ ###
jar -cfM img_boss.jar img_boss.dat

pause
