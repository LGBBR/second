@ECHO OFF

del *.dat
del *.jar

REM ### ���X�g���݂ăt�@�C�����A�[�J�C�u ###
..\tools\mycopy.pl /o /F /Y img.list img.dat

REM ### JAR�A�[�J�C�u ###
jar -cfM img.jar img.dat

pause
