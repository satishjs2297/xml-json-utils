<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output omit-xml-declaration="yes" indent="yes" />

	<xsl:param name="removeDelayElements" select="'delay'" />
	<xsl:param name="removeValidElements" select="'valid'" />
	<xsl:param name="removeLocElements" select="'loc'" />

	<xsl:variable name="vdStart" select="//ev/valid/@start" />
	<xsl:variable name="vdEnd" select="//ev/valid/@end" />
	
	<xsl:variable name="lonValue" select="//ev/loc/geo/@lon" />
	<xsl:variable name="latValue" select="//ev/loc/geo/@lat" />
	<xsl:variable name="tableId" select="//ti/@table" />
	<xsl:variable name="tmcIdValue" select="//ev/loc/start/@id" />
	<xsl:variable name="tmcDirValue" select="//ev/loc/start/@dir" />

	<xsl:template match="@lang" />
	<xsl:template match="ti/@table" />
	<xsl:template match="ti/@tv" />
	<xsl:template match="ti/@fv" />
	<xsl:template match="ti/@at" />

	<xsl:template match="node()|@*" name="identity">
		<xsl:copy>
			<xsl:apply-templates select="node()|@*" />
		</xsl:copy>
	</xsl:template>

	<xsl:template match="ev/id">
		<xsl:element name="_id">
			<xsl:value-of select="." />
		</xsl:element>
	</xsl:template>

	<xsl:template match="ev/ec">
		<xsl:element name="eventCode">
			<xsl:value-of select="." />
		</xsl:element>
	</xsl:template>

	<xsl:template match="ev/se">
		<xsl:element name="severity">
			<xsl:value-of select="." />
		</xsl:element>
		<xsl:if test="//ev/loc/@type ='geo'">
			<xsl:element name="geo">
				<type>Point</type>
				<coordinates>				
					<xsl:value-of select="$lonValue" />
					<xsl:value-of select="$latValue" />
				</coordinates>
				<roadName>AB-40</roadName>
			</xsl:element>
		</xsl:if>
		<xsl:if test="//ev/loc/@type ='tmc'">
			<xsl:element name="tmc">
				<table><xsl:value-of select="$tableId" /></table>
				<id><xsl:value-of select="$tmcIdValue" /></id>
				<direction><xsl:value-of select="$tmcDirValue" /></direction>
			</xsl:element>
		</xsl:if>
	</xsl:template>
	
	<xsl:template match="ev/text">
		<xsl:element name="description">
			<xsl:value-of select="." />
		</xsl:element>
		<validStart>
			<xsl:value-of select="$vdStart" />
		</validStart>
		<validEnd>
			<xsl:value-of select="$vdEnd" />
		</validEnd>
		<type>TrafficIncident</type>
		<lastUpdated>
			<xsl:value-of select="current-dateTime()" />
		</lastUpdated>
	</xsl:template>

	<xsl:template match="*">
		<xsl:if
			test="not(name() = $removeDelayElements 
			or name() = $removeValidElements or name() = $removeLocElements)">
			<xsl:call-template name="identity" />
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
