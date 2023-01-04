package in.geofriend.tableuibuilder;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TableUIBuilder {
    private final Context context;
    private final HashMap<Integer, HashMap<Integer, CellProperty>> cellProperties = new HashMap<>();
    private final HashMap<Integer, CellProperty> rowProperties = new HashMap<>();
    private final HashMap<Integer, CellProperty> colProperties = new HashMap<>();
    private CellProperty defaultProperty;
    private final String [][] cells;
    private int maxColumns = 0;
    private TableDataAdapter dataBinder;
    private int tableMarginLeft = 1;
    private int tableMarginRight = 1;
    private int tableMarginTop = 1;
    private int tableMarginBottom = 1;
    private int tableBackgroundColor = 0;

    public TableUIBuilder(Context context, String csvFilePath) {
        this.context = context;
        String csvContent = readFileFromAsset(context, csvFilePath);
        String[] lines = csvContent.split("\n");
        cells = new String[lines.length][];
        for(int i=0; i<lines.length; i++) {
            cells[i] = lines[i].split(",");
            maxColumns = Math.max(maxColumns, cells[i].length);
        }
        dataBinder = new TableDataAdapter();
        tableBackgroundColor = Color.BLACK;
        defaultProperty = new CellProperty();
        defaultProperty.textColor = Color.BLACK;
        defaultProperty.backgroundColor = Color.WHITE;

    }

    public TableUIBuilder setTableMargin(int left, int right, int top, int bottom) {
        tableMarginBottom = bottom;
        tableMarginLeft = left;
        tableMarginRight = right;
        tableMarginTop = top;
        return this;
    }

    public TableUIBuilder setTableBackgroundColor(int color) {
        tableBackgroundColor = color;
        return this;
    }
    public TableUIBuilder setColSpanForRow(int row, int span) {
        if(rowProperties.get(row) == null) {
            rowProperties.put(row, new CellProperty());
        }
        Objects.requireNonNull(rowProperties.get(row)).colSpan = span;
        return this;
    }

    public TableUIBuilder setColSpanForCell(int row, int column, int span) {
        initializeCellPropertyIfRequired(row,column);
        Objects.requireNonNull(Objects.requireNonNull(cellProperties.get(row)).get(column)).colSpan = span;
        return this;
    }

    public TableUIBuilder setRowBackgroundColor(int color, int row) {
        if(rowProperties.get(row) == null) {
            rowProperties.put(row, new CellProperty());
        }
        Objects.requireNonNull(rowProperties.get(row)).backgroundColor = color;
        return this;
    }

    public TableUIBuilder setRowTextColor(int color, int row) {
        if(rowProperties.get(row) == null) {
            rowProperties.put(row, new CellProperty());
        }
        Objects.requireNonNull(rowProperties.get(row)).textColor = color;
        return this;
    }

    public TableUIBuilder setCellBackgroundColor(int color, int row, int column) {
        if(cellProperties.get(row) == null) {
            cellProperties.put(row, new HashMap<>());
        }
        if(Objects.requireNonNull(cellProperties.get(row)).get(column) == null) {
            Objects.requireNonNull(cellProperties.get(row)).put(column, new CellProperty());
        }
        Objects.requireNonNull(Objects.requireNonNull(cellProperties.get(row)).get(column)).backgroundColor = color;
        return this;
    }

    public TableUIBuilder setCellTextColor(int color, int row, int column) {
        if(cellProperties.get(row) == null) {
            cellProperties.put(row, new HashMap<>());
        }
        if(Objects.requireNonNull(cellProperties.get(row)).get(column) == null) {
            Objects.requireNonNull(cellProperties.get(row)).put(column, new CellProperty());
        }
        Objects.requireNonNull(Objects.requireNonNull(cellProperties.get(row)).get(column)).textColor = color;
        return this;
    }

    public TableUIBuilder setCellTextAlignment(int alignment, int row, int column) {
        initializeCellPropertyIfRequired(row, column);
        Objects.requireNonNull(Objects.requireNonNull(cellProperties.get(row)).get(column)).textAlign = alignment;
        return this;
    }

    public TableUIBuilder setCellTextStyle(int style, int row, int column) {
        initializeCellPropertyIfRequired(row, column);
        Objects.requireNonNull(Objects.requireNonNull(cellProperties.get(row)).get(column)).textStyle = style;
        return this;
    }

    public TableUIBuilder setRowTextAlignment(int alignment, int row) {
        initializeRowPropertyIfRequired(row);
        Objects.requireNonNull(rowProperties.get(row)).textAlign = alignment;
        return this;
    }

    public TableUIBuilder setRowTextStyle(int style, int row) {
        initializeRowPropertyIfRequired(row);
        Objects.requireNonNull(rowProperties.get(row)).textStyle = style;
        return this;
    }

    public TableUIBuilder setDefaultTextAlignment(int alignment) {
        defaultProperty.textAlign = alignment;
        return this;
    }

    public TableUIBuilder setDefaultTextStyle(int style) {
        defaultProperty.textStyle = style;
        return this;
    }

    public TableUIBuilder setDefaultCellBackgroundColor(int color) {
        if(defaultProperty == null) {
            defaultProperty = new CellProperty();
        }
        defaultProperty.backgroundColor = color;
        return this;
    }

    public TableUIBuilder setDefaultTextColor(int color) {
        if(defaultProperty == null) {
            defaultProperty = new CellProperty();
        }
        defaultProperty.textColor = color;
        return this;
    }

    public TableUIBuilder setDefaultBorderColor(int color) {
        if(defaultProperty == null) {
            defaultProperty = new CellProperty();
        }
        defaultProperty.borderColor = color;
        return this;
    }

    public TableUIBuilder bind(TableDataAdapter binder) {
        if(binder == null) {
            binder = new TableDataAdapter();
        }
        dataBinder = binder;
        return this;
    }

    private void initializeCellPropertyIfRequired(int row, int column) {
        if(cellProperties.get(row) == null) {
            cellProperties.put(row, new HashMap<>());
        }
        if(Objects.requireNonNull(cellProperties.get(row)).get(column) == null) {
            Objects.requireNonNull(cellProperties.get(row)).put(column, new CellProperty());
        }
    }

    private void initializeRowPropertyIfRequired(int row) {
        if(rowProperties.get(row) == null) {
            rowProperties.put(row, new CellProperty());
        }
        if(rowProperties.get(row)== null) {
            Objects.requireNonNull(rowProperties.put(row, new CellProperty()));
        }
    }
    public View build() {
        TableLayout tableLayout = new TableLayout(context);
        tableLayout.setStretchAllColumns(true);
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tableLayoutParams.setMargins(tableMarginLeft, tableMarginTop, tableMarginRight, tableMarginBottom);
        tableLayout.setBackgroundColor(tableBackgroundColor);
        tableLayout.setLayoutParams(tableLayoutParams);
        tableLayout.setShrinkAllColumns(true);
        for (int i = 0; i < cells.length; i++) {
            int rowInstances = dataBinder.getRowCount(i);
            for(int rowInstance = 0; rowInstance < rowInstances; rowInstance++) {
                TableRow row = new TableRow(context);
                TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT);
                tableRowParams.setMargins(1, 1, 1, 1);
                tableRowParams.weight = 1;
                row.setLayoutParams(tableRowParams);
                CellProperty rowProperty = rowProperties.get(i);
                if(rowProperty != null && rowProperty.backgroundColor != null) {
                    row.setBackgroundColor(rowProperty.backgroundColor);
                }
                for (int j = 0; j < cells[i].length; j++) {
                    TableRow.LayoutParams cellParames = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    cellParames.setMargins(1, 1, 1, 1);
                    TextView cellContent = new TextView(context);
                    cellContent.setGravity(Gravity.CENTER);
                    String cell = getBindedData(cells[i][j], i, rowInstance);
                    cellContent.setText(cell);
                    CellProperty cellProperty = getCellProperty(i, j);
                    if (cellProperty != null) {
                        if (cellProperty.backgroundColor != null) {
                            cellContent.setBackgroundColor(cellProperty.backgroundColor);
                        }
                        if (cellProperty.textColor != null) {
                            cellContent.setTextColor(cellProperty.textColor);
                        }
                        if (cellProperty.colSpan >= 1) {
                            cellParames.span = cellProperty.colSpan;
                        }
                        if(cellProperty.textStyle != null) {
                            cellContent.setTypeface(null, cellProperty.textStyle);
                        }
                        if(cellProperty.textAlign != null) {
                            cellContent.setGravity(cellProperty.textAlign);
                        }
                    } else {
                        cellContent.setBackgroundColor(Color.WHITE);
                    }
                    row.addView(cellContent, cellParames);
                }
                tableLayout.addView(row);
            }

        }
        return tableLayout;
    }

    private String getBindedData(String cell, int templateRowIndex, int instance) {
        if(dataBinder == null) {
            return cell;
        }
        ArrayList<String> tokens = new ArrayList<>();
        StringBuilder token = new StringBuilder();
        for(int i=0; i<cell.length(); i++) {
            char ch = cell.charAt(i);
            if(ch == '{') {
                if(token.length() > 0) {
                    tokens.add(token.toString());
                    token = new StringBuilder();
                }
                token.append(ch);
            } else if(ch == '}'){
                token.append(ch);
                tokens.add(token.toString());
                token = new StringBuilder();
            } else {
                token.append(ch);
            }
        }
        if(token.length() > 0) {
            tokens.add(token.toString());
        }
        StringBuilder finalValue = new StringBuilder();
        for(String tkn : tokens) {
            if(tkn.startsWith("{") && tkn.endsWith("}")) {
                tkn = dataBinder.getValue(tkn.replace("{","").replace("}","").trim(), templateRowIndex, instance);
            }
            finalValue.append(tkn);
        }
        return finalValue.toString();
    }

    private CellProperty getCellProperty(int row, int col) {
        CellProperty cellProperty = new CellProperty();
        if(cellProperties.get(row) != null && Objects.requireNonNull(cellProperties.get(row)).get(col) != null) {
            cellProperty =  Objects.requireNonNull(cellProperties.get(row)).get(col);
        }
        if(rowProperties.get(row) != null) {
            CellProperty rowProperty = rowProperties.get(row);
            assert cellProperty != null;
            mergeProperty(cellProperty, rowProperty);
        }
        if(defaultProperty != null) {
            assert cellProperty != null;
            mergeProperty(cellProperty, defaultProperty);
        }
        return cellProperty;
    }

    private void mergeProperty(CellProperty cellProperty, CellProperty propertyToBeMerge) {
        if(cellProperty.textColor == null) {
            cellProperty.textColor = propertyToBeMerge.textColor;
        }
        if(cellProperty.backgroundColor == null) {
            cellProperty.backgroundColor = propertyToBeMerge.backgroundColor;
        }
        if(cellProperty.borderColor == null) {
            cellProperty.borderColor = propertyToBeMerge.borderColor;
        }
        if(cellProperty.colSpan < 0) {
            cellProperty.colSpan = propertyToBeMerge.colSpan;
        }

        if(cellProperty.row < 0) {
            cellProperty.row = propertyToBeMerge.row;
        }
        if(cellProperty.col < 0) {
            cellProperty.col = propertyToBeMerge.col;
        }

        if(cellProperty.textAlign == null) {
            cellProperty.textAlign = propertyToBeMerge.textAlign;
        }

        if(cellProperty.textStyle == null) {
            cellProperty.textStyle = propertyToBeMerge.textStyle;
        }
    }

    private static String readFileFromAsset(Context context, String fileName) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream ims = assetManager.open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(ims));
            StringBuilder builder = new StringBuilder();
            String str = reader.readLine();
            while (str != null) {
                builder.append(str);
                str = reader.readLine();
                if(str != null) {
                    builder.append("\n");
                }
            }
            reader.close();
            ims.close();
            return builder.toString();
        } catch (Exception e) {
            throw new RuntimeException("Some exception occurred while reading file " + fileName, e);
        }
    }

    public static class TableDataAdapter {
        public String getValue(String columnName, int templateRowIndex, int index) {
            return "";
        }

        public int getRowCount(int templateRowIndex) {
            return 1;
        }
    }

    private static class CellProperty {
        int colSpan = -1;
        Integer backgroundColor = null;
        Integer textColor = null;
        Integer borderColor = null;
        int row = -1;
        int col = -1;
        Integer textStyle = null;
        Integer textAlign = null;
    }
}
