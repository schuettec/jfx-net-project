#!/bin/bash

echo "Preparing gradle.properties"

cat <<EOF > ~/.m2/settings.xml
<settings>
  <servers>
    <server>
      <id>ossrh</id>
      <username>${envOssrhUser}</username>
      <password>${envOssrhPassword}</password>
    </server>
    <server>
      <id>gpg.passphrase</id>
      <passphrase>clear or encrypted text</passphrase>
    </server>
  </servers>
</settings>
EOF

echo "Completed m2.settings"

echo "Decrypting Sign Key..."

md5sum ./etc/sign.enc
openssl aes-256-cbc -d -pass "pass:$envKeyringPassword" -in ./etc/sign.enc -out ./etc/secring-decrypted.gpg
md5sum ./etc/secring-decrypted.gpg

echo "Starting task 'uploadArchives'..."
./gradlew uploadArchives -Prelease

