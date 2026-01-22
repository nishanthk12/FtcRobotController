package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous
public class TestAutoMecanum extends OpMode {

    private DcMotor FrontLeft, FrontRight, BackLeft, BackRight;

    @Override
    public void init() {

        FrontLeft = hardwareMap.get(DcMotor.class, "front_left");
        FrontRight = hardwareMap.get(DcMotor.class, "front_right");
        BackLeft = hardwareMap.get(DcMotor.class, "back_left");
        BackRight = hardwareMap.get(DcMotor.class, "back_right");

        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        BackRight.setDirection(DcMotor.Direction.REVERSE);

        FrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    @Override
    public void start() {

        driveAuto(0.25, 0.0, 0.0);
        try { Thread.sleep(2000); } catch (Exception e) {}
        driveAuto(-0.25, 0.0, 0.0);
        try { Thread.sleep(2000); } catch (Exception e) {}
        driveAuto(0.0, 0.25, 0.0);
        try { Thread.sleep(2000); } catch (Exception e) {}
        driveAuto(0.0, -0.25, 0.0);
        try { Thread.sleep(2000); } catch (Exception e) {}
        driveAuto(0.0, 0.0, 0.0);
        try { Thread.sleep(2000); } catch (Exception e) {}

    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {
        driveAuto(0.0, 0.0, 0.0);
    }

    public void driveAuto(double forward, double strafe, double rotate) {
        double FrontLeftPower = forward - strafe - rotate;
        double FrontRightPower = forward + strafe + rotate;
        //double BackLeftPower = -forward - strafe + rotate;
        double BackLeftPower = forward + strafe - rotate;
        double BackRightPower = forward - strafe + rotate;

        double maxPower = 0.25;
        double maxSpeed = 0.25;

        maxPower = Math.max(maxPower, Math.abs(FrontLeftPower));
        maxPower = Math.max(maxPower, Math.abs(FrontRightPower));
        maxPower = Math.max(maxPower, Math.abs(BackLeftPower));
        maxPower = Math.max(maxPower, Math.abs(BackRightPower));

        FrontLeft.setPower(maxSpeed * FrontLeftPower / maxPower);
        FrontRight.setPower(maxSpeed * FrontRightPower / maxPower);
        BackLeft.setPower(maxSpeed * BackLeftPower / maxPower);
        BackRight.setPower(maxSpeed * BackRightPower / maxPower);
    }
}
