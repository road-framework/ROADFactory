<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
	<xsl:output method="xml" indent="yes" />
	<xsl:param name="GetService.doneMsg" />
	<xsl:template match="/">
		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
			<soapenv:Body>
				<q0:GetServiceResponse xmlns:q0="http://ws.apache.org/axis2"
					xmlns:q1="http://jaxws.core/">
					<return>
						<capabilities>
							<actions>
								<name>
									<xsl:value-of
										select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/capabilities/actions/name" />
								</name>
								<raasRef>
									<xsl:value-of
										select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/capabilities/actions/raasRef" />
								</raasRef>
							</actions>
							<name>
								<xsl:value-of
									select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/capabilities/name" />
							</name>
							<raasRef>
								<xsl:value-of
									select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/capabilities/raasRef" />
							</raasRef>
						</capabilities>
						<granularity>
							<xsl:value-of
								select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/granularity" />
						</granularity>
						<guid>
							<xsl:value-of
								select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/guid" />
						</guid>
						<name>
							<xsl:value-of
								select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/name" />
						</name>
						<nature>
							<xsl:value-of
								select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/nature" />
						</nature>
						<provider>
							<xsl:value-of
								select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/provider" />
						</provider>
						<publicationTime>
							<xsl:value-of
								select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/publicationTime" />
						</publicationTime>
						<raasRef>
							<xsl:value-of
								select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/raasRef" />
						</raasRef>
						<releaseStage>
							<xsl:value-of
								select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/releaseStage" />
						</releaseStage>
						<technicalInterfaces>
							<raasRef>
								<xsl:value-of
									select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/technicalInterfaces/raasRef" />
							</raasRef>
							<technicalOperations>
								<name>
									<xsl:value-of
										select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/technicalInterfaces/technicalOperations/name" />
								</name>
								<raasRef>
									<xsl:value-of
										select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/technicalInterfaces/technicalOperations/raasRef" />
								</raasRef>
							</technicalOperations>
							<type>
								<xsl:value-of
									select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/technicalInterfaces/type" />
							</type>
						</technicalInterfaces>
						<version>
							<xsl:value-of
								select="$GetService.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/version" />
						</version>
					</return>
				</q0:GetServiceResponse>
			</soapenv:Body>
		</soapenv:Envelope>
	</xsl:template>
</xsl:stylesheet>