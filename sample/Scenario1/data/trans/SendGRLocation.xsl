<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
	xmlns:q0="http://ws.apache.org/axis2">
	<xsl:output method="xml" indent="yes" />
	<xsl:param name="PlaceRepairOrder.doneMsg" />
	<xsl:template match="/">
		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" >

			<soapenv:Body>
				<q0:sendGRLocation xmlns:q0="http://ws.apache.org/axis2">
					<content>
						<xsl:value-of select="$PlaceRepairOrder.doneMsg/soapenv:Envelope/soapenv:Body/q0:placeRepairOrderResponse/return" />
					</content>
				</q0:sendGRLocation>
			</soapenv:Body>
		</soapenv:Envelope>
	</xsl:template>
</xsl:stylesheet>