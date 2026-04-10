plugins {
    id("java")
    id("war")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    compileOnly("javax.servlet:javax.servlet-api:4.0.1")

    implementation("org.thymeleaf:thymeleaf:3.0.15.RELEASE")
    implementation(libs.guava)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.test {
    useJUnitPlatform()
}