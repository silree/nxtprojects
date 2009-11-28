/**
 * <p>Classes in this package is used to calculate coordinate conversion.</p>
 * <p>
 * There are two coordinates in NXTRockBoy system: 
 * <ul>
 * <li>NXT LCD coordinate - a coordinate attached to LCD. 
 * The origin is on the most top-left corner, 
 * and the positive direction of x-axis is right 
 * while the positive direction of y-axis is down. 
 * </li> 
 * <li>Nature coordinate - a coordinate attached to the nature environment. 
 * The origin is the central point of the rotation motor, 
 * and the positive direction of x-axis is right
 * while the positive direction of y-axis is up. 
 * </li>
 * </ul>
 * </p> 
 * <p>
 * There are 3 main classes currently: 
 * <ul>
 * <li>McAngle - describe angles in the multi-coordinate</li>
 * <li>McVector - describe vectors in the multi-coordinate</li>
 * <li>McPoint - describe points in the multi-coordinate</li>
 * </ul>
 * And <code>McObject</code> is the super class of all these classes. 
 * </p>
 * 
 * @author Programus
 */
package org.programus.nxj.rockboy.core.mc; 