LATEST_VERSION="1.1.0-as4.0_beta4"

# Download source code
wget "https://github.com/theapache64/jaba/archive/v$LATEST_VERSION.zip" -O "jaba.zip" &&

# Download JAR
wget "https://github.com/theapache64/jaba/releases/download/v$LATEST_VERSION/jaba.jar" &&

# Unzip
unzip jaba.zip &&

# Rename
mv "jaba-$LATEST_VERSION" jaba &&

# Move jar to unzipped folder
mv jaba.jar jaba/ &&

realJarPath=$(realpath jaba/jaba.jar)

echo "alias jaba='java -jar $realJarPath'" >> "~/.bash_aliases" &&

echo "👍 Installed"


