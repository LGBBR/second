@ECHO OFF

del *.dat
del *.jar

REM ### ���X�g���݂ăt�@�C�����A�[�J�C�u ###
..\tools\mycopy.pl /o /F /Y img_boss.list img_boss.dat

REM ### JAR�A�[�J�C�u ###
jar -cfM img_boss.jar img_boss.dat

pause
