<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
	xmlns:q0="http://ws.apache.org/axis2">
	<xsl:output method="xml" indent="yes" />
	<xsl:param name="CO-GR.orderRepair.Res" />
	<xsl:template match="/">
		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" >

			<soapenv:Body>
				<q0:payGR xmlns:q0="http://ws.apache.org/axis2">
					<content>
						<xsl:value-of select="$CO-GR.orderRepair.Res/soapenv:Envelope/soapenv:Body/q0:orderRepairResponse/return" />
					</content>
				</q0:payGR>
			</soapenv:Body>
		</soapenv:Envelope>
	</xsl:template>
</xsl:stylesheet>