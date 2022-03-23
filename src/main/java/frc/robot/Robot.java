// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.VictorSP;
import edu.wpi.first.cameraserver.CameraServer;


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot 
{

  private final VictorSP m_leftDrive = new VictorSP(7); // front
  private final VictorSP m_leftDrive2 = new VictorSP(6); // rear
  private final VictorSP m_rightDrive = new VictorSP(1); // front 9
  private final VictorSP m_rightDrive2 = new VictorSP(2); // rear 8

  private final VictorSP m_sushi = new VictorSP(8); // UNBIND THIS
  private final VictorSP m_elevator = new VictorSP(3);

  private final DifferentialDrive m_robotDrive = new DifferentialDrive(m_leftDrive, m_rightDrive);
  private final DifferentialDrive m_robotDrive2 = new DifferentialDrive(m_leftDrive2, m_rightDrive2);
  private final Joystick m_stick = new Joystick(0);
  private final Timer m_timer = new Timer();


  // this is for all the magic numbers and variables and stuff
  private boolean sushitoggle = false;
  private double sushispeed = 0.5;
  private double sushispeedModifier = 0.1;
  private double turnMultiplier = 0.575;
  

  /**
 * Effectively only a solution so that we don't have to retype the same two lines of code.
 * Takes the same first two values as arcadeDrive().
 * <p>Might add more functionality later idk lol.
 * @param x The same as arcadeDrive().
 * @param y The same as arcadeDrive().
 */
  public void DriveAll(double x, double y) 
  {
    m_robotDrive.arcadeDrive(m_stick.getY(), -turnMultiplier * m_stick.getX());
    m_robotDrive2.arcadeDrive(m_stick.getY(), -turnMultiplier * m_stick.getX());
  }

  /**
   * Wait for a specified amount of milliseconds. Might cause unforeseen errors, so ONLY USE IN TEST MODE. 
   * Also probably not a good idea to use it in periodic mode for anything.
   * @param ms The amount of milliseconds to wait.
   */
  public static void wait(int ms)
  {
    try
    {
        Thread.sleep(ms);
    }
    catch(InterruptedException ex)
    {
        Thread.currentThread().interrupt();
    }
  }

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() 
  {
    CameraServer.startAutomaticCapture();
    m_leftDrive.setInverted(true);
    m_leftDrive2.setInverted(true);

    // !!!
    // CHANGE THIS BEFORE COMPETITION
    // Not sure which side is going to be the "front" yet, so it needs to be tested on the actual robot
  }

  /** This function is run once each time the robot enters autonomous mode. */
  @Override
  public void autonomousInit() 
  {
    m_timer.reset();
    m_timer.start();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() 
  {
    // Drive for 2 seconds
    if (m_timer.get() < 2.0)
    {
      m_leftDrive.set(-0.5);
      m_leftDrive2.set(-0.5);
      m_rightDrive2.set(-0.5);
      m_rightDrive.set(-0.5);
    }
    else
    {
      m_robotDrive.stopMotor();
    }

  }

  
  /** This function is called once each time the robot enters teleoperated mode. */
  @Override
  public void teleopInit() {
    sushitoggle = false;
  }

  /** This function is called periodically during teleoperated mode. */
  @Override
  //m_stick.getRawButton(7) == true
 /*public void teleopPeriodic() 
  {
    if (m_stick.getRawButtonPressed(7) ^ m_stick.getRawButtonPressed(5))
    {
      if (sushitoggle == false)
      {
        if (m_stick.getRawButtonReleased(7))
        {
          m_sushi.set(1);
          sushitoggle = true;
        }
        if (m_stick.getRawButtonReleased(5))
        {
          m_sushi.set(0.5);
          sushitoggle = true;
        }
      }
      else
      {
        m_sushi.set(0);
        sushitoggle = false;
      }
    }

    DriveAll(m_stick.getY(), m_stick.getX());
  }*/
  public void teleopPeriodic() 
  {
    if (m_stick.getRawButtonPressed(7))
    {
      if (sushitoggle == false)
      {
        sushispeed = 0.5;
        m_sushi.set(sushispeed);
        sushitoggle = true;
      }
      else
      {
        m_sushi.set(0);
        sushitoggle = false;
      }
    }
    if (m_stick.getPOV() != -1 && sushitoggle == true)
    {
      if (m_stick.getPOV() == 0 && sushispeed < 1)
      {
        if (sushispeed + sushispeedModifier == 0)
        {
          sushispeed += (sushispeedModifier * 2);
        }
        else
        {
        sushispeed += sushispeedModifier;
        }
        m_sushi.set(sushispeed);
      }
      if (m_stick.getPOV() == 180 && sushispeed > -1)
      {
        if (sushispeed - sushispeedModifier == 0)
        {
          sushispeed -= (sushispeedModifier * 2);
        }
        else
        {
        sushispeed -= sushispeedModifier;
        }
        m_sushi.set(sushispeed);
      }
    } 
    //System.out.print(Double.toString(sushispeed) + ", " + Double.toString(sushispeedModifier));
    DriveAll(m_stick.getY(), m_stick.getX());
  }
  /** This function is called once each time the robot enters test mode. */
  @Override
  public void testInit()
  {
    m_leftDrive.set(0.5);
    wait(1000);
    m_leftDrive.stopMotor();
    m_leftDrive2.set(0.5);
    wait(1000);
    m_leftDrive2.stopMotor();
    m_rightDrive.set(0.5);
    wait(1000);
    m_rightDrive.stopMotor();
    m_rightDrive2.set(0.5);
    wait(1000);
    m_rightDrive2.stopMotor();
  }
  

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() 
  {
    
  }
}

/**
*
88888888888                 888          
    888                     888          
    888                     888          
    888   .d88b.        .d88888  .d88b.  
    888  d88""88b      d88" 888 d88""88b 
    888  888  888      888  888 888  888 
    888  Y88..88P      Y88b 888 Y88..88P 
    888   "Y88P"        "Y88888  "Y88P"  
               
* Add more functionality to DriveAll() (perhaps a built in inverter?)
* Add more features to the robot that are actually useful 
* Make sure the custom wait() thing I made never errors in init functions
* Maybe try and see if using wait() in periodic functions causes it to error? I would think it does
because of the weird interrupt thing Java has but idk
 */