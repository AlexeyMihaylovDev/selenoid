

resource "aws_instance" "my_Amazon_linux" {
  # count                  = var.prefix
  ami                         = "ami-0a1ee2fb28fe05df3" #Amazon Linux AMI
  instance_type               = "t3.medium"
  vpc_security_group_ids      = [aws_security_group.EX1_polybot-secure-group.id]
  subnet_id                   = aws_subnet.public-subnet-2b.id
  associate_public_ip_address = true
  key_name                    = "alexeymihaylov_key"
  user_data                   = file("script.sh")
  #  depends_on                  = [aws_vpc.vpc, aws_autoscaling_group.Polybot-aws_autoscaling_group]
  tags = {
    Name        = "selenoid server"
    environment = "tf"
  }
}

