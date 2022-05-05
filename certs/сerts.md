# Генерация хранилища сертификатов
```
keytool -genkeypair -alias free-project -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore free-project.p12 -validity 3650
```

# Получение приватного и публичного ключа
``` 
openssl pkcs12 -in free-project.p12 -nocerts -out free-project.key
openssl pkcs12 -in free-project.p12 -clcerts -nokeys -out free-project.crt 
```

# Удаление пароля у приватного ключа
```
openssl rsa -in free-project.key -out free-project_without_password.key
```