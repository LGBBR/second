@ECHO OFF

del *.dat
del *.jar

REM ### ���X�g���݂ăt�@�C�����A�[�J�C�u ###
..\tools\mycopy.pl /o /F /Y img_zako.list img_zako.dat

REM ### JAR�A�[�J�C�u ###
jar -cfM img_zako.jar img_zako.dat

pause
