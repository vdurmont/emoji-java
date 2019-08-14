# Deployment Notes

*This is intended as a reminder for @vdurmont to deploy this library to maven central.*

## GPG signing

Import your gpg key: `gpg --import path/to/my/private/key.asc`

## Sonatype auth

Setup your `~/.m2/settings.xml` file:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
    https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>ossrh</id>
      <username>MY_USERNAME</username>
      <password>MY_SONATYPE_PASSWORD</password>
    </server>
  </servers>
</settings>
```

## Deploy!

```
mvn clean deploy -DperformRelease=true -Prelease
```