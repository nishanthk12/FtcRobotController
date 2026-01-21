package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

import java.util.Locale;


@Autonomous
public class TestPinpointDriveToPoint extends LinearOpMode{

    DcMotor FrontLeft, FrontRight, BackLeft, BackRight;

    GoBildaPinpointDriver odo;
    TestDriveToPoint nav = new TestDriveToPoint();

    enum StateMachineTest {
        WAITING_FOR_START,
        AT_TARGET,
        DRIVE_TO_TARGET_1
    }

    static final Pose2D TARGET_1 = new Pose2D(DistanceUnit.MM,2000,0,AngleUnit.DEGREES,0);

    @Override
    public void runOpMode() {


        FrontLeft = hardwareMap.get(DcMotor.class, "front_left");
        FrontRight = hardwareMap.get(DcMotor.class, "front_right");
        BackLeft = hardwareMap.get(DcMotor.class, "back_left");
        BackRight = hardwareMap.get(DcMotor.class, "back_right");

        FrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        FrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        BackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        FrontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        FrontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        BackRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        FrontRight.setDirection(DcMotor.Direction.REVERSE);
        BackRight.setDirection(DcMotor.Direction.REVERSE);

        odo = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        odo.setOffsets(-88.9, -190.5, DistanceUnit.MM); //these are tuned for 3110-0002-0001 Product Insight #1
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.REVERSED, GoBildaPinpointDriver.EncoderDirection.FORWARD);


        //odo.recalibrateIMU();
        odo.resetPosAndIMU();

        //nav.setDriveType(TestDriveToPoint.DriveType.MECANUM);

        StateMachineTest stateMachinetest;
        stateMachinetest = StateMachineTest.WAITING_FOR_START;

        telemetry.addData("Status", "Initialized");
        telemetry.addData("X offset", odo.getXOffset(DistanceUnit.MM));
        telemetry.addData("Y offset", odo.getYOffset(DistanceUnit.MM));
        telemetry.addData("Device Version Number:", odo.getDeviceVersion());
        telemetry.addData("Device Scalar", odo.getYawScalar());
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();
        resetRuntime();

        while (opModeIsActive()) {

            odo.update();

            switch (stateMachinetest){

                case WAITING_FOR_START:
                    //the first step in the autonomous
                    stateMachinetest = StateMachineTest.DRIVE_TO_TARGET_1;
                    break;
                case DRIVE_TO_TARGET_1:
                    /*
                    drive the robot to the first target, the nav.driveTo function will return true once
                    the robot has reached the target, and has been there for (holdTime) seconds.
                    Once driveTo returns true, it prints a telemetry line and moves the state machine forward.
                     */
                    if (nav.driveTo(odo.getPosition(), TARGET_1, 0.25, 0)){
                        telemetry.addLine("at position #1!");
                        stateMachinetest = StateMachineTest.AT_TARGET;
                    }
                    break;
            }

            //nav calculates the power to set to each motor in a mecanum or tank drive. Use nav.getMotorPower to find that value.
            FrontLeft.setPower(nav.getMotorPower(TestDriveToPoint.DriveMotor.LEFT_FRONT));
            FrontRight.setPower(nav.getMotorPower(TestDriveToPoint.DriveMotor.RIGHT_FRONT));
            BackLeft.setPower(nav.getMotorPower(TestDriveToPoint.DriveMotor.LEFT_BACK));
            BackRight.setPower(nav.getMotorPower(TestDriveToPoint.DriveMotor.RIGHT_BACK));

            telemetry.addData("current state:",stateMachinetest);

            Pose2D pos = odo.getPosition();
            String data = String.format(Locale.US, "{X: %.3f, Y: %.3f, H: %.3f}", pos.getX(DistanceUnit.MM), pos.getY(DistanceUnit.MM), pos.getHeading(AngleUnit.DEGREES));
            telemetry.addData("Target Position 1", data);

            telemetry.update();
        }
    }


}
