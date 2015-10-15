/** 
 * Author: Diana Ly 
 * Interactive line graph based on abortions in the US from 
 * 1968 to 2013 
 *
 * Data Source: 
 * http://www.johnstonsarchive.net/policy/abortion/usab-misc.html
 *
 * Additional resources:
 * http://www.benfry.com and Visualizing Data by Ben Fry
*/

Table data;

float dataMin, dataMax;
int yearMin, yearMax;
int yearInterval;
int[] years;

int yInterval;

PFont plotFont;

float plotX1, plotY1;
float plotX2, plotY2;
float labelX, labelY;

int currentColumn = 0;
int columnCount;

void setup( ) {
  size(800, 600);
  
  data = new Table("Abortion.csv");
  columnCount = data.getColumnCount( );
  
  years = int(data.getRowNames( ));
  
  yearMin = years[0];
  yearMax = years[years.length - 1];
  yearInterval = 5;
 
  dataMin = 0;
  yInterval = (int) data.getColumnMax(currentColumn) / 3;
  dataMax = ceil(data.getTableMax( ) / yInterval) * yInterval;
  
  // Corners of the plotted time series
  plotX1 = 70;
  plotX2 = width - 30;  
  plotY1 = 50;
  plotY2 = height - 50;
  labelX = 35;
  labelY = height - 10;
  
  plotFont = createFont("SansSerif", 20);
  textFont(plotFont);
  
  
  smooth( );
}

void draw( ) {
  background(0);
  
  // Show the plot area as a white box.
  fill(255);
  rectMode(CORNERS);
  noStroke();
  rect(plotX1, plotY1, plotX2, plotY2);

  drawTitle();

  strokeWeight(5);  
  stroke(#5679C1);
  
  drawDataPoints(currentColumn);
  drawYearLabels();
  drawVolumeLabels();
  drawAxisLabels();
  
  noFill();
  drawDataLine(currentColumn);
  drawDataHighlight(currentColumn);
}

void drawDataLine(int col) {
  beginShape();
  int rowCount = data.getRowCount( );
  for (int row = 0; row < rowCount; row++) {
    if (data.isValid(row, col)) {
      float value = data.getFloat(row, col);
      float x = map(years[row], yearMin, yearMax, plotX1, plotX2);
      float y = map(value, data.getColumnMin(currentColumn), data.getColumnMax(currentColumn), plotY2, plotY1);
      stroke(348, 30, 95);
      strokeWeight(1.5);
      vertex(x, y);
    }
  }
  endShape( );
}

void drawDataHighlight(int col) {
  for (int row = 0; row < data.getRowCount(); row++) {
    if (data.isValid(row, col)) {
      float value = data.getFloat(row, col);
      float x = map(years[row], yearMin, yearMax, plotX1, plotX2);
      float y = map(value, data.getColumnMin(currentColumn), data.getColumnMax(currentColumn), plotY2, plotY1);
      
      if (dist(mouseX, mouseY, x, y) < 3) {
        strokeWeight(10);
        stroke(x, y, row);
        point(x, y);
        fill(#8F46F7);
        textSize(15);
        
        if (mouseX < width/2)
          textAlign(LEFT);
        else
          textAlign(RIGHT);

        if (value < data.getColumnMax(currentColumn)/2) {
          text("Year: " + years[row] + "\nAbortion Figure: " + nfc((int)value), x, y - 10);
        } else {
          text("Year: " + years[row] + "\nAbortion Figure: " + nfc((int)value), x, y + 35);
        }
      }
    }
   }
}

// Draw the data as a series of points.
void drawDataPoints(int col) {
  int rowCount = data.getRowCount( );
  noStroke();
  for (int row = 0; row < rowCount; row++) {
    if (data.isValid(row, col)) {
      float value = data.getFloat(row, col);
      float x = map(years[row], yearMin, yearMax, plotX1, plotX2);
      float y = map(value, data.getColumnMin(currentColumn), data.getColumnMax(currentColumn), plotY2, plotY1);
      stroke(230, 23, 0);
      point(x, y);
    }
  }
}


void drawVolumeLabels( ) {
  
  yInterval = ceil((int) data.getColumnMax(currentColumn) / 6);
  
  stroke(128);
  strokeWeight(1);

  dataMax = ceil(data.getColumnMax(currentColumn) / yInterval) * yInterval;
  fill(0);
  textSize(13.5);
  textAlign(RIGHT, CENTER);
  
  for (float v = 0; v < dataMax; v += yInterval) {
    float y = map(v, dataMin, dataMax, plotY2, plotY1);
    String value = nfc(floor(v));
    fill(225);
    text(value, plotX1 - 5, y);
    line(plotX1 - 4, y, plotX1, y); // Draw major tick
  }
  smooth();
}


void drawAxisLabels( ) {
  fill(180);
  textSize(15);
  textLeading(15);
  textAlign(CENTER, CENTER);
  text("Abortions\nFigures", labelX, plotY1 + 10);
  textAlign(CENTER);
  text("Year", (plotX1+plotX2)/2, labelY);
  smooth();
}

void drawTitle( ) {
  fill(#FF0000);
  textSize(25);
  textAlign(LEFT);
  String title = data.getColumnName(currentColumn);
  text(title, plotX1, plotY1-10);
}

void drawYearLabels() {
  fill(255);
  textSize(13);
  textAlign(LEFT,CENTER);
  stroke(225);
  strokeWeight(0);
  
  for (int row = 0; row < data.rowCount; row++) {
    if (years[row] % yearInterval == 0) {
      float x = map(years[row], yearMin, yearMax, plotX1, plotX2);
      text(years[row], x, plotY2 + 10);
      line(x, plotY1, x, plotY2);
    }
  }
}

void keyPressed( ) {
  if (key == '[') {
    currentColumn--;
    if (currentColumn < 0) {
      currentColumn = columnCount - 1;
    }
  } else if (key == ']') {
    currentColumn++;
    if (currentColumn == columnCount) {
      currentColumn = 0;
    }
  }
}

void mouseClicked( ) {
  if (mouseButton == LEFT) {
    currentColumn--;
    if (currentColumn < 0) {
      currentColumn = columnCount - 1;
    }
  } else if (mouseButton == RIGHT) {
    currentColumn++;
    if (currentColumn == columnCount) {
      currentColumn = 0;
    }
  }
}
