<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="text" indent="yes"/>

    <xsl:template match="/">

        <xsl:value-of select="concat('Ship Order #', shiporder/@orderid, '&#10;')" />

        <xsl:value-of select="concat('To: ', shiporder/@shipto, '&#10;')" />

        <xsl:value-of select="concat('Address: ', shiporder/@address, '&#10;')" />

        <xsl:value-of select="concat('City: ', shiporder/@city, '&#10;')" />

        <xsl:value-of select="concat('Country: ', shiporder/@country, '&#10;')" />

        <xsl:value-of select="'-------------------------------------------&#10;'" />
        <xsl:for-each select="shiporder/item">
            <xsl:value-of select="concat('Title: ', title, '&#9;Quantity: ', quantity, '&#9;Price: ', price, '&#10;')" />
        </xsl:for-each>
        <xsl:value-of select="'-------------------------------------------&#10;'" />

        <xsl:variable name="count" select="sum(shiporder/item/quantity)"/>
        <xsl:value-of select="concat('Total Count: ', $count, '&#10;')" />

        <xsl:variable name="total" select="sum(shiporder/item/price)"/>
        <xsl:value-of select="concat('Total Price: ', $total, '&#10;')" />

        <xsl:variable name="average" select="sum(shiporder/item/price) div $count"/>
        <xsl:value-of select="concat('Average Price: ', floor($average * 100) div 100, '&#10;')" />

    </xsl:template>

</xsl:stylesheet>