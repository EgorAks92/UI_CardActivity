@ECHO OFF
where gradle >NUL 2>NUL
IF %ERRORLEVEL% NEQ 0 (
  ECHO ERROR: Gradle is not installed. Please install Gradle or regenerate wrapper with 'gradle wrapper'.
  EXIT /B 1
)
CALL gradle %*
