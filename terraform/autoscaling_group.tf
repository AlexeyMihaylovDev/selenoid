resource "aws_autoscaling_group" "Polybot-aws_autoscaling_group" {
  name                 = "${var.project_name}-autoscaling-group"
  desired_capacity     = 0
  max_size             = 3
  min_size             = 0
  vpc_zone_identifier  = [aws_subnet.public-subnet-1a.id, aws_subnet.public-subnet-2b.id]
  default_cooldown     = 60
  launch_configuration = aws_launch_configuration.launch_config.name
  force_delete         = true
  tag {
    key                 = "worker-alexey-dima"
    propagate_at_launch = true
    value               = "ec2 instance"

  }
}

resource "aws_launch_configuration" "launch_config" {
  image_id        = "ami-044fc6ddbb6094b68"
  instance_type   = "t2.micro"
  key_name        = "alexeymihaylov_key"
  user_data       = file("script.sh")
  name            = "selenoid"
  security_groups = [aws_security_group.EX1_polybot-secure-group.id]


}
