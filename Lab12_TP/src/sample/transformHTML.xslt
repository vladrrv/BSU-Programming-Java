<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:variable name="count" select="sum(shiporder/item/quantity)"/>
    <xsl:variable name="total" select="sum(shiporder/item/price)"/>

    <xsl:template match="/">
        <html>
            <head>
                <style>
                    table {
                        border-spacing: 0px;
                        border: 1px solid #d56912;
                    }
                    td, th {
                        padding: 5px 4px 5px 4px;
                        border: 1px solid #d0b5a2;
                        color: #634b38;
                    }
                    table tr:nth-child(odd) td {
                        background-color: #f6dcc8;
                    }
                    table tr:nth-child(even) td {
                        background-color: #fff2e9;
                    }
                </style>
            </head>
            <body style="font-family:verdana;">
                <h2>Ship Order #<xsl:value-of select="shiporder/@orderid"/></h2>
                <table border="1">
                    <colgroup>
                        <col width="120"/>
                        <col width="120"/>
                    </colgroup>
                    <tr>
                        <th>Title</th>
                        <th>Quantity</th>
                        <th>Price</th>
                    </tr>
                    <xsl:for-each select="shiporder/item">
                        <tr>
                            <td><xsl:value-of select="title"/></td>
                            <td><xsl:value-of select="quantity"/></td>
                            <td><xsl:value-of select="price"/></td>
                        </tr>
                    </xsl:for-each>
                    <tr>
                        <th>Total</th>
                        <th><xsl:value-of select="$count" /></th>
                        <th><xsl:value-of select="$total" /></th>
                    </tr>
                </table>
            </body>
        </html>

    </xsl:template>

</xsl:stylesheet>