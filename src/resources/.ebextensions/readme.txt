This folder exists for customizing the server at the Linux level prior to load of the Server Instance.

To add a new EBS volume to be automatically mounted, create the volume in AWS console, then edit the .config file to attach the volume by name.

If this is a NEW EBS volume, you will need to attach to an instance, login to ssh, then create ext3:

sudo mkfs.ext3 /dev/sdh

1. create EBS volume (same availability zone as the AMI instance!)
2. attach EBS to an instance
3. ssh to instance
4. (if new) sudo mkfs.ext3 /dev/sdh
5. edit the .config to use the new volume ID to auto-attach in future instance launches

The .config is based upon this instruction:
http://aws.typepad.com/aws/2012/10/customize-elastic-beanstalk-using-configuration-files.html

MySQL driver installation please see the following for more information:
https://forums.aws.amazon.com/thread.jspa?messageID=421387

This is a working example of attaching a named EBS volume at boot
packages:
  yum:
    xorg-x11-server-Xvfb: []
  
  
  packages:
  yum:
    mysql-connector-java: []

container_commands:
  00install-mysql-connector-java:
    command: "/bin/ln -s /usr/share/java/mysql-connector-java.jar /usr/share/tomcat7/lib"
    ignoreErrors: true
  
  
  
commands:
  01-attach-volume:
    command: |
      export JAVA_HOME=/usr/lib/jvm/jre && \
      export EC2_HOME=/opt/aws/apitools/ec2 && \
      export IN_USE=$(/opt/aws/bin/ec2-describe-volumes  vol-2758227f --hide-tags \
        --aws-access-key AWS_ACCESS_KEY \
        --aws-secret-key AWS_SECRET_KEY \
        | grep "in-use") && \
      if [ -z "${IN_USE}" ]; then
        /opt/aws/bin/ec2-attach-volume vol-2758227f \d
          -i $(/opt/aws/bin/ec2-metadata --instance-id | cut -c14-) \
          -d /dev/xvdf \
          --aws-access-key AWS_ACCESS_KEY \
          --aws-secret-key AWS_SECRET_KEY &&
        mkdir -p /media-volume &&
        sleep 30 && \
        mount /dev/xvdf /media-volume -t ext3 && \
        chown -R tomcat.tomcat /media-volume
      fi

  02-setup-xvfb:
    command: "Xvfb :1 -screen 0 1280x768x24 &"
    ignoreErrors: true
      