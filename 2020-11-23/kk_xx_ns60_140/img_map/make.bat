@ECHO OFF

del *.dat
del *.jar

REM ### ���X�g���݂ăt�@�C�����A�[�J�C�u ###
..\tools\mycopy.pl /o /F /Y img_map.list img_map.dat

REM ### JAR�A�[�J�C�u ###
jar -cfM img_map.jar img_map.dat

pause
