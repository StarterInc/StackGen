# get updates
sudo yum update -y
sudo yum install -y docker

# start docker
sudo service docker start
sudo usermod -a -G docker ec2-user

# create data folder
cd ~
mkdir data

# run che
sudo docker run -it --rm -v /var/run/docker.sock:/var/run/docker.sock -v /home/ec2-user/data:/data eclipse/che start